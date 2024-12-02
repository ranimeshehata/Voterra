/* eslint-disable no-unused-vars */
import { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import { userState, isAuthenticatedState } from '../recoil/atoms';
import { jwtDecode } from 'jwt-decode';

const useAuth = () => {
  const [user, setUser] = useRecoilState(userState);
  const [isAuthenticated, setIsAuthenticated] = useRecoilState(isAuthenticatedState);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds

        if (decodedToken.exp > currentTime) {
          setIsAuthenticated(true);
          setUser(decodedToken);
        } else {
          // Token has expired
          setIsAuthenticated(false);
          setUser(null);
          localStorage.removeItem('token');
        }
      } catch (error) {
        setIsAuthenticated(false);
        setUser(null);
      }
    }

    const handleStorageChange = event => {
      if (event.key === 'logout') {
        setIsAuthenticated(false);
        setUser(null);
      } else if (event.key === 'token' && event.newValue) {
        const decodedToken = jwtDecode(event.newValue);
        const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds

        if (decodedToken.exp > currentTime) {
          setIsAuthenticated(true);
          setUser(decodedToken);
        } else {
          // Token has expired
          setIsAuthenticated(false);
          setUser(null);
          localStorage.removeItem('token');
        }
      }
    };

    window.addEventListener('storage', handleStorageChange);
    return () => {
      window.removeEventListener('storage', handleStorageChange);
    };
  }, [setIsAuthenticated, setUser]);

  return { user, isAuthenticated };
};

export default useAuth;