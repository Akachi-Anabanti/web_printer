import axios from 'axios';

const API_URL = '/api/print';

export const uploadFile = async (file, params) => {
  const formData = new FormData();
  formData.append('file', file);
  
  try {
    const response = await axios.post(`${API_URL}/upload`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      params: params
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

export const getPrinterStatus = async () => {
  try {
    const response = await axios.get(`${API_URL}/status`);
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};
