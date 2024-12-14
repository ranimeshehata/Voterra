/* eslint-disable no-unused-vars */
import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { userState, isAuthenticatedState } from '../recoil/atoms';
import { Col, Row, Container } from 'react-bootstrap';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from 'react-router-dom';
import useFetch from '../hooks/useFetch';
import Header from '../components/Header';
import SideBarHomePage from '../components/SideBarHomePage';
import UserFeed from '../components/UserFeed';

function HomePage() {
  const user = useRecoilValue(userState);
  const isAuthenticated = useRecoilValue(isAuthenticatedState);
  const navigate = useNavigate();
  const { postSignout } = useFetch();

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
      },
      ()=>{}
    );
  };
  
  return (

    <div className="homepage-container bg-white-100 w-full absolute top-0">
      <div className="header">
        <Header />
      </div>

      <div className="main-content">
        <div className="sidebar">
          <SideBarHomePage user={user} handleLogout={handleLogout} />
        </div>

        <div className="feed">
          <UserFeed />
        </div>
      </div>

      {/* <Row className="home-page d-flex flex-row">
      <Col className="sidebar-wrap align-items-center d-flex justify-content-center">
        <SideBarHomePage />
      </Col>
      <Col xs={12} md={8} xl={9}>
        <UserFeed />
      </Col>
    </Row> */}
{/* 
      <div className="welcome-message">
        <p>Welcome, {user ? user.firstName + '!' : 'Voterra user!'}</p>
      </div>
      <div className="logout-button">
        <button onClick={handleLogout} className="btn btn-primary">
          Sign Out
        </button>
      </div> */}
      <ToastContainer />
    </div>
  );
}

export default HomePage;
