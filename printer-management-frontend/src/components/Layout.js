import React from 'react';

const Layout = ({ children }) => {
  return (
    <div className="layout">
      <header>
        <h1>Printer Management System</h1>
      </header>
      <main>{children}</main>
      <footer>
        <p>&copy; 2024 Printer Management System</p>
      </footer>
    </div>
  );
};

export default Layout;