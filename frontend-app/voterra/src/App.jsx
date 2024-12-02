import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import React, { Suspense } from 'react';
import './style.css';
const Login = React.lazy(() => import('./pages/Login'));
const IntroPage = React.lazy(() => import('./pages/IntroPage'));
const SignUp = React.lazy(() => import('./pages/SignUp'));

function App() {
  return (
    <div>
      <Router>
        <Suspense>
          <Routes>
            <Route path="/" element={<IntroPage />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<SignUp />} />
          </Routes>
        </Suspense>
      </Router>
    </div>
  );
}

export default App;
