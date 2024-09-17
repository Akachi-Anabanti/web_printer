const express = require('express');
const multer = require('multer');
const cors = require('cors');
const { printer } = require('node-printer');

const app = express();
const upload = multer({ dest: 'uploads/' });

const corsOptions = {
  origin: "https://effective-acorn-pv44jrwv9jph6vjx-5173.app.github.dev/", // Allow only this origin
  methods: ['GET', 'POST'], // Allow only GET and POST requests
  allowedHeaders: ['Content-Type', 'Authorization'], // Allow these headers
  optionsSuccessStatus: 200 // For legacy browser support
};



app.use(cors(corsOptions));
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