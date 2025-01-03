import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './style.css';
import React, { Suspense } from 'react';
import './style.css';
import useAuth from './hooks/useAuth';
const Login = React.lazy(() => import('./pages/Login'));
const IntroPage = React.lazy(() => import('./pages/IntroPage'));
const UserProfile = React.lazy(() => import('./pages/UserProfile'));
const SignUp = React.lazy(() => import('./pages/SignUp'));
const HomePage = React.lazy(() => import('./pages/HomePage'));
const ResetPasswordPage = React.lazy(() => import('./pages/ResetPasswordPage'));
const SavedPosts = React.lazy(() => import('./pages/SavedPosts'));
const ReportedPosts = React.lazy(() => import('./pages/ReportedPosts'));
const ErrorNotFound = React.lazy(() => import('./pages/ErrorNotFound'));
const AboutPage = React.lazy(() => import('./pages/AboutPage'));
const ContactsPage = React.lazy(() => import('./pages/ContactsPage'));

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
            <Route path='/savedPosts' element={<SavedPosts />} />
            <Route path="/resetpassword" element={<ResetPasswordPage />} />
            <Route path="/userprofile" element={<UserProfile />} />
            <Route path="/reportedposts" element={<ReportedPosts />} />
            <Route path="*" element={<ErrorNotFound />} />
            <Route path="/about" element={<AboutPage />} />
            <Route path="/contactus" element={<ContactsPage />} />
            
          </Routes>
        </Suspense>
      </Router>
    </div>
  );
}

export default App;
