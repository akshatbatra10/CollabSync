import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { ThemeProvider } from "./context/ThemeContext.tsx";
import { AppWrapper } from "./components/common/PageMeta.tsx";
import { TaskModalProvider } from "./context/TaskModalContext.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <TaskModalProvider>
      <ThemeProvider>
        <AppWrapper>
          <App />
        </AppWrapper>
      </ThemeProvider>
    </TaskModalProvider>
  </StrictMode>
);
