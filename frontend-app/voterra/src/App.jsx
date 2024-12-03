import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './style.css';
import React, { Suspense } from 'react';
import './style.css';
import useAuth from './hooks/useAuth';
const Login = React.lazy(() => import('./pages/Login'));
const IntroPage = React.lazy(() => import('./pages/IntroPage'));
const SignUp = React.lazy(() => import('./pages/SignUp'));
const HomePage = React.lazy(() => import('./pages/HomePage'));
const ResetPasswordPage = React.lazy(() => import('./pages/ResetPasswordPage'));

function App() {
  useAuth();

  return (
    <div>
      <Router>
        <Suspense>
          <Routes>
            <Route path="/" element={<IntroPage />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/homepage" element={<HomePage />} />
            <Route path="/resetpassword" element={<ResetPasswordPage />} />
          </Routes>
        </Suspense>
      </Router>
    </div>
  );
}

export default App;
