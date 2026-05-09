export interface TextShare {
  id: string
  content: string
  device: string
  createdAt: string
  expiresAt: string
  viewCount: number
  isExpired: boolean
}

export interface CreateTextRequest {
  content: string
  device: string
}

export interface CreateTextResponse {
  id: string
  link: string
  expiresAt: string
  viewCount: number
}

export interface GetTextResponse {
  id: string
  content: string
  device: string
  viewCount: number
  createdAt: string
  expiresAt: string
  expired: boolean
}

export interface ViewCountResponse {
  viewCount: number
}

export interface ApiErrorResponse {
  success: false
  error: {
    code: string
    message: string
  }
}