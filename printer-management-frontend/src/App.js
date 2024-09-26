import React from 'react';
import Layout from './components/Layout';
import FileUpload from './components/FileUpload';
import PrinterStatus from './components/PrinterStatus';

function App() {
  return (
    <Layout>
      <FileUpload />
      <PrinterStatus />
    </Layout>
  );
}

export default App;