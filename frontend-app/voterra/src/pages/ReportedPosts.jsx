/* eslint-disable no-unused-vars */
import { useEffect, useState } from 'react';
import { useRecoilValue, useRecoilState } from 'recoil';
import { userState, isAuthenticatedState } from '../recoil/atoms';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from 'react-router-dom';
import useFetch from '../hooks/useFetch';
import Header from '../components/Header';
import SideBarHomePage from '../components/SideBarHomePage';
import Loader from '../components/Loader';
import MainSectionReported from '../components/MainSectionReported';




function ReportedPosts() {
    const [user, setUser] = useRecoilState(userState);
    const [loading, setLoading] = useState(true);
    const isAuthenticated = useRecoilValue(isAuthenticatedState);
    const navigate = useNavigate();
    const { postSignout } = useFetch();
    
    useEffect(() => {
        const token = localStorage.getItem('token');
      if (!isAuthenticated) {
        if (!token || token === 'expired') {
          toast.error('Your session has expired. Please log in again.');
          navigate('/login');
        } else {
          toast.error('Invalid token. Please log in again.');
          navigate('/login');
        }
      } else {
        const storedUser = JSON.parse(localStorage.getItem('user'));
        if (storedUser){
          setUser(storedUser)
          const reportedPosts = JSON.parse(localStorage.getItem('reportedPosts')) || [];
            setUser(prevUser => ({
                ...prevUser,
                reportedPosts: reportedPosts
            }));
        }
        setLoading(false);
      }
    }, [isAuthenticated, navigate, setUser]);

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
      
      if (loading) {
        return (
        <div>
          <Loader />
        </div>
        );
      }

    return (
    <div className="homepage-container bg-white-100 w-full absolute top-0">
        <div className="header">
            <Header />
        </div>
  
        <div className="main-content">
          <div className="hidden md:block w-[20%]">
            <SideBarHomePage user={user} handleLogout={handleLogout}/>
          </div>
  
          <div className="w-[100%] md:w-[80%] lg:w-[60%] min-h-screen  p-3">
            <MainSectionReported />
          </div>
        </div>
      </div>
    )
}

export default ReportedPosts;