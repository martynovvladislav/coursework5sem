import axios from 'axios';

const API = axios.create({
  baseURL: 'http://rental_service:8080/api',
});

const PHOTO_API = axios.create({
  baseURL: 'http://rental_photo_service:8090/api'
});

API.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const fetchAllApartments = () => API.get('/apartments/all');
export const fetchMyApartments = () => API.get("/apartments/my");
export const fetchApartments = () => API.get('/apartments');
export const fetchApartment = (id) => API.get(`/apartments/${id}`);
export const checkAvailability = (id, start, end) =>
  API.get(`/apartments/${id}/availability`, { params: { start_day: start, end_day: end } });
export const deleteApartment = (id) => API.delete(`/apartments/${id}`);
export const publishApartment = (id, isPublished) =>
  API.patch(`/apartments/${id}/publish?isPublished=${isPublished}`);
export const saveApartment = (apartment) => {
  if (apartment.id) {
    return API.put(`/apartments/${apartment.id}`, apartment);
  } else {
    return API.post("/apartments", apartment);
  }
};
export const uploadPhoto = (formData) => {
  return PHOTO_API.post('/photos/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

export const login = (data) => API.post('/auth/login', data);
export const register = (data) => API.post('/auth/register', data);

export const createBooking = (data) => API.post('/bookings', data);
export const getBookings = (params) => API.get('/bookings', { params });
export const getBookingRequests = (params) => API.get('/bookings/requests', { params });
export const deleteBooking = (id) => API.delete(`/bookings/${id}`);
export const updateBookingStatus = (id, status) =>
  API.patch(`/bookings/${id}/status`, null, { params: { status } });



