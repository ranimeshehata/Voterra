/* eslint-disable no-unused-vars */
import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { userState, isAuthenticatedState } from '../recoil/atoms';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from 'react-router-dom';
import useFetch from '../hooks/useFetch';
import Header from '../components/Header';

function HomePage() {
  const user = useRecoilValue(userState);
  const isAuthenticated = useRecoilValue(isAuthenticatedState);
  const navigate = useNavigate();
  const { post,postSignout } = useFetch();

  useEffect(() => {
    if (!isAuthenticated) {
      const token = localStorage.getItem('token');
      if (!token || token === 'expired') {
        toast.error('Your session has expired. Please log in again.');
        navigate('/login');
      } else {
        toast.error('Invalid token. Please log in again.');
        navigate('/login');
      }
    }
  }, [isAuthenticated, navigate]);

  const handleLogout = () => {
    const token = localStorage.getItem("token");
    console.log("Token:", token);
    if (!token) {
      toast.error("Session expired. Please log in again.");
      navigate("/login");
      return;
    }
  
    postSignout(
      "http://localhost:8080/users/signout",
      {
        token
      },
      (response, error) => {
        console.log("Response:", response.message);
        console.log("Error:", error);
        console.log("Token:", token);
        if(response.message){
          localStorage.removeItem("token");
          localStorage.removeItem("user");
          console.log("Logout successful:", response.message);
          navigate("/login");
          toast.success("User signed out successfully!");
        }
        else{
          console.error("Logout failed:", error);
          toast.error("Error signing out. Please try again.");
        }
      },()=>{}
    );
  };
  
  
  
  

  return (
    <div className="bg-white-100 w-full absolute top-0">
      <div className="header">
        <Header />
      </div>

      <div className="welcome-message">
        <p>Welcome, {user ? user.firstName + '!' : 'Voterra user!'}</p>
      </div>

      <div className="logout-button">
        <button onClick={handleLogout} className="btn btn-primary">
          Sign Out
        </button>
      </div>

      <ToastContainer />
    </div>
  );
}

export default HomePage;
