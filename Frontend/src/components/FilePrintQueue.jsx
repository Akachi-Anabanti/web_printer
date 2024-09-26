import { useState, useCallback, useEffect } from "react";
import { Upload, FileText, Printer } from "lucide-react";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";

const FileUploadPrintQueue = () => {
  const [queue, setQueue] = useState([]);
  const [currentFile, setCurrentFile] = useState(null);
  const [progress, setProgress] = useState(0);
  const [printStatus, setPrintStatus] = useState("");
  const [printJobId, setPrintJobId] = useState(null);
  const [serverStatus, setServerStatus] = useState("Offline");

  const handleFileUpload = useCallback((event) => {
    const files = Array.from(event.target.files);
    setQueue((prevQueue) => [...prevQueue, ...files]);
  }, []);

  const uploadAndPrint = async (file) => {
    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch("http://localhost:3001/upload-and-print", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        throw new Error("Upload failed");
      }

      const result = await response.json();
      setPrintJobId(result.jobId);
      setPrintStatus("Printing...");
    } catch (error) {
      console.error("Error:", error);
      setPrintStatus("Error occurred while printing");
    }
  };

  const checkPrintStatus = useCallback(async () => {
    if (printJobId) {
      try {
        const response = await fetch(
          `http://localhost:3001/print-status/${printJobId}`
        );
        const result = await response.json();
        setPrintStatus(result.status);

        if (result.status === "completed") {
          setPrintJobId(null);
        }
      } catch (error) {
        console.error("Error checking print status:", error);
      }
    }
  }, [printJobId]);

  useEffect(() => {
    const statusInterval = setInterval(checkPrintStatus, 5000);
    return () => clearInterval(statusInterval);
  }, [checkPrintStatus]);

  useEffect(() => {
    fetch("http://localhost:3001/")
      .then((response) => response.json())
      .then((data) => setServerStatus(data.message));
  }, [serverStatus]);

  const processNextFile = useCallback(async () => {
    if (queue.length > 0 && !currentFile) {
      const nextFile = queue[0];
      setCurrentFile(nextFile);
      setQueue((prevQueue) => prevQueue.slice(1));

      await uploadAndPrint(nextFile);

      setTimeout(() => {
        setCurrentFile(null);
        setProgress(0);
        if (queue.length > 0) {
          processNextFile();
        }
      }, 2000);
    }
  }, [queue, currentFile]);

  useEffect(() => {
    processNextFile();
  }, [processNextFile]);

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-lg shadow-xl">
      <h1 className="text-2xl font-bold mb-4">File Upload and Print Queue</h1>
      <h3 className="bg-secondary">{serverStatus}</h3>
      <div className="mb-4">
        <label
          htmlFor="file-upload"
          className="cursor-pointer bg-blue-500 text-white py-2 px-4 rounded inline-flex items-center"
        >
          <Upload className="mr-2" />
          <span>Upload Files</span>
        </label>
        <input
          id="file-upload"
          type="file"
          multiple
          onChange={handleFileUpload}
          className="hidden"
        />
      </div>
      {queue.length > 0 && (
        <Alert>
          <FileText className="h-4 w-4" />
          <AlertTitle>Queue Status</AlertTitle>
          <AlertDescription>{queue.length} file(s) in queue</AlertDescription>
        </Alert>
      )}
      {currentFile && (
        <div className="mt-4">
          <h2 className="text-lg font-semibold mb-2">
            Processing: {currentFile.name}
          </h2>
          <div className="w-full bg-gray-200 rounded-full h-2.5 dark:bg-gray-700">
            <div
              className="bg-blue-600 h-2.5 rounded-full"
              style={{ width: `${progress}%` }}
            ></div>
          </div>
        </div>
      )}
      {printStatus && (
        <Alert className="mt-4">
          <Printer className="h-4 w-4" />
          <AlertTitle>Print Status</AlertTitle>
          <AlertDescription>{printStatus}</AlertDescription>
        </Alert>
      )}
    </div>
  );
};

export default FileUploadPrintQueue;
