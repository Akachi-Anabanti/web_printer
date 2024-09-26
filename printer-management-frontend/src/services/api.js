import axios from 'axios';

const API_URL = 'https://effective-acorn-pv44jrwv9jph6vjx-5000.app.github.dev/api/print'; //'http://localhost:5000/api/print';

export const uploadFile = async (file) => {
  const formData = new FormData();
  formData.append('file', file);
  
  try {
    const response = await axios.post(`${API_URL}/upload`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
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