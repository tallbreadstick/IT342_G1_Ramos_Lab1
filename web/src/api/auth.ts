import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token & log request
axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  console.log('[Axios Request]', config.method?.toUpperCase(), config.url, config.data);
  return config;
});

// Response interceptor to log responses & errors
axiosInstance.interceptors.response.use(
  (response) => {
    console.log('[Axios Response]', response.status, response.config.url, response.data);
    return response;
  },
  (error) => {
    console.error('[Axios Error]', error.response?.status, error.response?.data, error.message);
    return Promise.reject(error);
  }
);

// Interfaces
export interface User {
  id: number;
  username: string;
  email: string;
  profileImage?: string;
  dateJoined: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface LoginRequest {
  usernameOrEmail: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

// Register function
export const register = async (data: RegisterRequest): Promise<User> => {
  try {
    const response = await axiosInstance.post<User>('/auth/register', data);
    console.log('[Register Success]', response.data);
    return response.data;
  } catch (error) {
    console.error('[Register Failed]', error);
    throw error;
  }
};

// Login function
export const login = async (data: LoginRequest): Promise<AuthResponse> => {
  try {
    const response = await axiosInstance.post<AuthResponse>('/auth/login', data);
    console.log('[Login Success]', response.data);
    return response.data;
  } catch (error) {
    console.error('[Login Failed]', error);
    throw error;
  }
};

// Get profile function
export const getProfile = async (): Promise<User> => {
  try {
    const response = await axiosInstance.get<User>('/user/me');
    console.log('[Profile Success]', response.data);
    return response.data;
  } catch (error) {
    console.error('[Profile Failed]', error);
    throw error;
  }
};

// Server-side logout function
export const logoutServer = async (): Promise<void> => {
  try {
    const response = await axiosInstance.post('/auth/logout');
    console.log('[Logout Server Success]', response.status);
  } catch (error) {
    console.error('[Logout Server Failed]', error);
    throw error;
  }
};

// Logout function
export const logout = (): void => {
  localStorage.removeItem('authToken');
  console.log('[Logout Local] Token removed from localStorage');
};

// Save token function
export const saveToken = (token: string): void => {
  localStorage.setItem('authToken', token);
  console.log('[Save Token] Token saved to localStorage');
};

// Get token function
export const getToken = (): string | null => {
  return localStorage.getItem('authToken');
};

// Check if user is authenticated
export const isAuthenticated = (): boolean => {
  return !!localStorage.getItem('authToken');
};
