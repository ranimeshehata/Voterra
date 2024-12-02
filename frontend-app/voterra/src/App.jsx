import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './style.css';
import Login from './pages/Login';
import HomePage from './pages/HomePage';
import IntroPage from './pages/IntroPage';
import React, { Suspense } from 'react';
import './style.css';
import useAuth from './hooks/useAuth';
// const Login = React.lazy(() => import('./pages/Login'));
// const IntroPage = React.lazy(() => import('./pages/IntroPage'));
const SignUp = React.lazy(() => import('./pages/SignUp'));

function App() {
  useAuth(); // Initialize authentication state

  return (
    <div>
      <Router>
        <Suspense>
          <Routes>
            <Route path="/" element={<IntroPage />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/homepage" element={<HomePage />} />
          </Routes>
        </Suspense>
      </Router>
    </div>
  );
}

export default App;