import React from 'react';
import UserProfileHeader from "../components/UserProfileHeader";

const UserProfile = ({user}) => {

  return (
    <div className="bg-white-100 w-full absolute top-0">
      <div className="header">
        <UserProfileHeader />
      </div>
      <div className="flex-grow flex flex-col items-center justify-center w-full">
        <div className="profile-header flex items-center mb-4">
          <img 
            src={user.profilePicture} 
            alt={`${user.name}'s profile`} 
            className="profile-picture mr-4" 
          />
          <h2 className="text-2xl">{user.name}</h2>
        </div>
        <div className="profile-posts w-full max-w-md">
          <h3 className="text-xl mb-2">Posts</h3>
          <ul className="list-disc pl-5">
            {user.posts.map((post, index) => (
              <li key={index} className="mb-1">{post}</li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default UserProfile;