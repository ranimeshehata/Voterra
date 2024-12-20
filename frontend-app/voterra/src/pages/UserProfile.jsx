import { useCallback, useEffect, useRef, useState } from 'react';
import UserProfileHeader from "../components/UserProfileHeader";
import { useRecoilState } from 'recoil';
import { userState } from '../recoil/atoms';
import { fetchUserProfilePosts } from '../voterraUtils/PostUtils';
import PostContainer from '../components/PostsContainer';
import Loader from '../components/Loader';

const UserProfile = () => {
      const [posts, setPosts] = useState([]);
      const [page, setPage] = useState(0);
      const [isLoading, setIsLoading] = useState(false);
      const [hasMore, setHasMore] = useState(true);
      const observerRef = useRef(null);
      const [user,setUser]=useRecoilState(userState);
      const loadPosts = useCallback(async (page) => {
          if (isLoading || !hasMore || !user) return;
          setIsLoading(true);
          try {
              const data = await fetchUserProfilePosts(page,user.email);
              if (data.length > 0) {
                  setPosts((prevPosts) => [...prevPosts, ...data]);
                  setPage(page + 1);
              } else {
                  setHasMore(false);
              }
          } catch (error) {
              console.error(error);
          } finally {
              setIsLoading(false);
          }
      }, [isLoading, hasMore,user]);
   
    useEffect(() => {
      
      const observer = new IntersectionObserver(
          (entries) => {
              if (entries[0].isIntersecting) {
                  loadPosts(page);
              }
          },
          { threshold: 1.0 }
      );
      const currentObserverRef = observerRef.current;
      if (currentObserverRef) observer.observe(currentObserverRef);

      return () => {
          if (currentObserverRef) observer.unobserve(currentObserverRef);
      };
  }, [page, hasMore, isLoading, loadPosts,user]);
  useEffect(()=>{
    const userObj=JSON.parse(localStorage.getItem('user'));
    setUser(userObj);
  },[]);
  
  return (
    <div className="bg-white-100 w-full">
      <div className="header">
        <UserProfileHeader />
      </div>
      {user&&<div className="flex-grow flex flex-col items-center justify-center w-full p-8">
        <div className="profile-header flex flex-col items-center mb-4">
          <h2 className="text-2xl">{user.firstName} {user.lastName}</h2>
          
          <h3 className="text-lg text-gray-500">@{user.username}</h3>
        </div>
        <div className="profile-posts w-full ">
          <h3 className="text-xl mb-2b ">Posts</h3>
          <PostContainer posts={posts}/>
          {isLoading && <Loader/>}
          {hasMore && <div ref={observerRef} className="infinite-trigger"></div>}
        </div>
      </div>}
    </div>
  );
};

export default UserProfile;