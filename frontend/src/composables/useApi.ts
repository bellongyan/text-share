import type {
  CreateTextRequest,
  CreateTextResponse,
  GetTextResponse,
  ViewCountResponse
} from '@/types'

const API_BASE = '/api/v1/texts'

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const error = await response.json() as { error?: { code: string; message: string } }
    throw new Error(error?.error?.message || `HTTP ${response.status}`)
  }
  return response.json()
}

export function useApi() {
  const createText = async (content: string, device: string): Promise<CreateTextResponse> => {
    const request: CreateTextRequest = { content, device }
    const response = await fetch(API_BASE, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(request)
    })
    const result = await handleResponse<{ success: boolean; data: CreateTextResponse }>(response)
    return result.data
  }

  const getText = async (id: string): Promise<GetTextResponse> => {
    const response = await fetch(`${API_BASE}/${id}`)
    const result = await handleResponse<{ success: boolean; data: GetTextResponse }>(response)
    return result.data
  }

  const incrementView = async (id: string): Promise<ViewCountResponse> => {
    const response = await fetch(`${API_BASE}/${id}/view`, { method: 'POST' })
    const result = await handleResponse<{ success: boolean; data: ViewCountResponse }>(response)
    return result.data
  }

  const getViewCount = async (id: string): Promise<number> => {
    const response = await fetch(`${API_BASE}/${id}/view`)
    const result = await handleResponse<{ success: boolean; data: ViewCountResponse }>(response)
    return result.data.viewCount
  }

  return { createText, getText, incrementView, getViewCount }
}