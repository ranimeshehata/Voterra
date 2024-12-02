import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { userState, isAuthenticatedState } from '../recoil/atoms';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function HomePage() {
  const user = useRecoilValue(userState);
  const isAuthenticated = useRecoilValue(isAuthenticatedState);

  useEffect(() => {
    if (!isAuthenticated) {
      toast.warn('Please log in to access all features.');
    }
  }, [isAuthenticated]);

  return (
    <div>
      <h1>Home Page</h1>
      <p>Welcome, {user ? user.firstName + " !" : 'Voterra user !'}</p>
      <ToastContainer />
    </div>
  );
}

export default HomePage;