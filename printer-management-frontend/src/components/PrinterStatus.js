import React, { useState, useEffect } from "react";
import { getPrinterStatus } from "../services/api";

const PrinterStatus = () => {
  const [status, setStatus] = useState("");

  useEffect(() => {
    const fetchStatus = async () => {
      try {
        const response = await getPrinterStatus();
        setStatus(response);
      } catch (error) {
        setStatus(
          `Error: ${"Server is not up yet, please wait \n or contact administrator"}`
        );
      }
    };

    fetchStatus();
    const interval = setInterval(fetchStatus, 5000); // Update every 5 seconds

    return () => clearInterval(interval);
  }, []);

  return (
    <div className="printer-status">
      <h2>Printer Status</h2>
      <p>{status}</p>
    </div>
  );
};

export default PrinterStatus;
