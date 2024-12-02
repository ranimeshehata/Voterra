import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { userState, isAuthenticatedState } from '../recoil/atoms';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from 'react-router-dom';

function HomePage() {
  const user = useRecoilValue(userState);
  const isAuthenticated = useRecoilValue(isAuthenticatedState);
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      const token = localStorage.getItem('token');
      if (!token) {
        toast.error('Your session has expired. Please log in again.');
        navigate('/login');
      } else if (token === 'expired') {
        toast.error('Your session has expired. Please log in again.');
        navigate('/login');
      } else {
        toast.error('Invalid token. Please log in again.');
        navigate('/login');
      }
    }
  }, [isAuthenticated, navigate]);

  return (
    <div>
      <h1>Home Page</h1>
      <p>Welcome, {user ? user.firstName + '!' : 'Voterra user!'}</p>
      <ToastContainer />
    </div>
  );
}

export default HomePage;
