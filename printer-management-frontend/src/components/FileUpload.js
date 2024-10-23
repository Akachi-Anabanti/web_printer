import React, { useState } from "react";
import { uploadFile } from "../services/api";
import PrintSettings from "./PrintSettings";

const FileUpload = () => {
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState("");
  const [printSettings, setPrintSettings] = useState({
    orientation: "portrait",
    pageSize: "a4",
    scale: 100,
    margin: "normal",
    backgroundGraphics: false,
    pageRange: "",
  });

  const handleSettingsChange = (e) => {
    const { name, value } = e;
    setPrintSettings((prevSettings) => ({ ...prevSettings, [name]: value }));
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!file) {
      setMessage("Please select a file first!");
      return;
    }
    try {
      const response = await uploadFile(file);

      setMessage(response);
    } catch (error) {
      setMessage(
        `Error: ${"Something ain't right yet, please wait \n or contact administrator"}`
      );
    } finally {
      setFile(null);
    }
  };

  return (
    <div className="file-upload">
      <h2>Upload File for Printing</h2>
      <form onSubmit={handleSubmit}>
        <input type="file" onChange={handleFileChange} />
        <PrintSettings
          settings={printSettings}
          onSettingsChange={handleSettingsChange}
        />
        <button type="submit">Upload and Print</button>
      </form>
      {message && <p className="message">{message}</p>}
    </div>
  );
};

export default FileUpload;
