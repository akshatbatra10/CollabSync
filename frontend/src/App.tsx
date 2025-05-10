import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Dashboard from "./pages/Dashboard";
import SignIn from "./pages/AuthPages/SignIn";
import SignUp from "./pages/AuthPages/SignUp";
import { Toaster } from "react-hot-toast";
import AppLayout from "./layout/AppLayout";
import Task from "./pages/Task";

const App: React.FC = () => {
  return (
    <Router>
      <Toaster
        position="top-center"
        toastOptions={{
          duration: 4000,
          style: {
            background: "white",
            color: "#1F2937", // gray-800
            border: "1px solid #22C55E", // green-500
            padding: "12px 16px",
            borderRadius: "10px",
            boxShadow: "0 4px 6px rgba(0,0,0,0.1)",
          },
          success: {
            iconTheme: {
              primary: "#22C55E", // green-500
              secondary: "#ECFDF5", // green-50
            },
          },
        }}
      />
      <Routes>
        <Route element={<AppLayout />}>
          <Route path="/" element={<Dashboard />} />
          <Route path="/tasks" element={<Task />} />
        </Route>
        <Route path="/signin" element={<SignIn />} />
        <Route path="/signup" element={<SignUp />} />
      </Routes>
    </Router>
  );
};

export default App;
