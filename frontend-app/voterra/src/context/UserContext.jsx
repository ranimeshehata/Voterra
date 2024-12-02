/* eslint-disable no-unused-vars */
import {
    createContext, useContext, useState, useEffect, useMemo
  }
    from 'react';
  import PropTypes from 'prop-types';
  import { jwtDecode } from 'jwt-decode';
  
  export const UserContext = createContext(null);
  
  export function UserProvider({ children }) {
    const [user, setUser] = useState(null);
    const [isAuthenticated, setIsAuthenticated]= useState(false);
    const token=localStorage.getItem('token');
    const login = () => setIsAuthenticated(true);
    // useEffect(() => {
    //   if (token) {
    //     try {
    //       const decodedToken = jwtDecode(token);
    //       const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds
  
    //       if (decodedToken.exp > currentTime) {
    //         setIsAuthenticated(true);
    //         setUser(decodedToken);
    //       } else {
    //         // Token has expired
    //         setIsAuthenticated(false);
    //         setUser(null);
    //         localStorage.removeItem('token');
    //       }
    //     } catch (error) {
    //       setIsAuthenticated(false);
    //       setUser(null);
    //     }
    //   }
    //   // check if any changes in localstorage
    //   const handleStorageChange = event => {
    //     if (event.key === 'logout') {
    //       setIsAuthenticated(false);
    //       setUser(null);
    //     } else if (event.key === 'token' && event.newValue) {
    //       const decodedToken = jwtDecode(event.newValue);
    //       const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds
  
    //       if (decodedToken.exp > currentTime) {
    //         setIsAuthenticated(true);
    //         setUser(decodedToken);
    //       } else {
    //         // Token has expired
    //         setIsAuthenticated(false);
    //         setUser(null);
    //         localStorage.removeItem('token');
    //       }
    //     }
    //   };
    //   window.addEventListener('storage', handleStorageChange);
    //   return () => {
    //     window.removeEventListener('storage', handleStorageChange);
    //   };
    // }, []);

    const contextValue = useMemo(() => ({
      user, setUser, isAuthenticated, login, token
    }), [user, isAuthenticated]);
  
    return (
      <UserContext.Provider value={contextValue}>
        {children}
      </UserContext.Provider>
    );
  }
  
  UserProvider.propTypes = {
    children: PropTypes.node.isRequired
  };
  
  export const useAuth = () => useContext(UserContext);
  