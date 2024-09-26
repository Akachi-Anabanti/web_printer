import React, { useState } from 'react';
import { uploadFile } from '../services/api';

const FileUpload = () => {
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState('');

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!file) {
      setMessage('Please select a file first!');
      return;
    }
    try {
      const response = await uploadFile(file);
      setMessage(response);
      setFile(null);
    } catch (error) {
      setMessage(`Error: ${error}`);
    }
  };

  return (
    <div className="file-upload">
      <h2>Upload File for Printing</h2>
      <form onSubmit={handleSubmit}>
        <input type="file" onChange={handleFileChange} />
        <button type="submit">Upload and Print</button>
      </form>
      {message && <p className="message">{message}</p>}
    </div>
  );
};

export default FileUpload;