const express = require('express');
const multer = require('multer');
const cors = require('cors');
const { printer } = require('node-printer');

const app = express();
const upload = multer({ dest: 'uploads/' });


app.use(cors({
  origin: 'http://localhost:5173', // Update this with your frontend URL
  methods: ['GET', 'POST', 'PUT', 'DELETE'],
  allowedHeaders: ['Content-Type'],
}));

app.use(express.json());

app.get("/", (req, res) => {
  res.status(200).send("Server is Active");
});

app.post('/upload-and-print', upload.single('file'), (req, res) => {
  if (!req.file) {
    return res.status(400).send('No file uploaded.');
  }
  console.log("I was hit!!")

  const filePath = req.file.path;
  
  // Get the default printer
  const defaultPrinter = printer.getDefaultPrinterName();
  
  // Create a print job
  const job = printer.printFile({
    filename: filePath,
    printer: defaultPrinter,
    success: function(jobID) {
      console.log("Sent to printer with ID: " + jobID);
      res.status(200).json({ message: 'File uploaded and sent to printer', jobId: jobID });
    },
    error: function(err) {
      console.error("Print error:", err);
      res.status(500).json({ message: 'Error occurred while printing', error: err });
    }
  });
});

app.get('/print-status/:jobId', (req, res) => {
  const jobId = req.params.jobId;
  
  printer.getJob(jobId, function(err, job) {
    if (err) {
      res.status(500).json({ message: 'Error getting job status', error: err });
    } else {
      res.status(200).json({ status: job.status });
    }
  });
});

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});