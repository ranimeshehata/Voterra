/* eslint-disable no-unused-vars */
import React, { useEffect, useState } from "react";
import { useRecoilState } from "recoil";
import { userState } from "../recoil/atoms";

const CreatePost = () => {
  const [polls, setPolls] = useState(["", ""]);
  const [show, setShow] = useState(false);
  const [privacy, setPrivacy] = useState("PUBLIC");
  const [category, setCategory] = useState("SPORTS");
  const [content, setContent] = useState(""); // Added state for textarea value
  const [user,setUser]=useRecoilState(userState);
  useEffect(()=>{
    console.log(user);
    
  },[])
  const categories = [
    "SPORTS", "TECHNOLOGY", "ENTERTAINMENT", "HEALTH", "EDUCATION",
    "BUSINESS", "FASHION", "FOOD", "JOBS", "MEDICAL", "CARS", "EVENTS",
    "PLACES", "CELEBRITIES", "NATURE", "MOVIES", "OTHER"
  ];

  const handlePollChange = (index, value) => {
    const updatedPolls = [...polls];
    updatedPolls[index] = value;
    setPolls(updatedPolls);
  };

  const addPoll = () => {
    if (polls.length < 10) {
      setPolls([...polls, '']);
    } else {
      alert('You can only add up to 10 polls.');
    }
  };

  const handlePrivacyChange = (e) => {
    setPrivacy(e.target.value);
  };

  const handleCategoryChange = (e) => {
    setCategory(e.target.value);
  };

  const handleContentChange = (e) => {
    setContent(e.target.value);
  };

  return (
    <div className="rounded-lg shadow-lg p-3 font-[nunito]">
      <h1 className="mb-2 text-xl">
        Content <i className="fa-regular fa-pen-to-square fa-sm"></i>
      </h1>
      <textarea
        placeholder="What's on your mind?... "
        maxLength={1000}
        value={content} 
        onChange={handleContentChange}
        onFocus={() => setShow(true)}
        className="bg-gray-100 w-full p-3 rounded-xl shadow-lg mb-2"
      ></textarea>
      <div className={`relative overflow-hidden ${show ? 'max-h-screen' : 'max-h-0'} transition-all ease-out duration-500`}>
        <div id="polls" className="p-1">
          <h1 className="mb-2 text-xl">Polls <i className="fa-solid fa-chart-simple fa-sm"></i></h1>
          {polls.map((poll, index) => (
            <input
              key={index}
              type="text"
              value={poll}
              onChange={(e) => handlePollChange(index, e.target.value)}
              placeholder={`Poll ${index + 1}`}
              className="bg-gray-100 w-full p-2 rounded-xl shadow-lg my-2"
            />
          ))}
        </div>
        <button
          onClick={addPoll}
          className="bg-red-500 mb-5 w-10 h-10 text-white p-2 rounded-full mt-2 shadow"
        >
          +
        </button>
        <div id="types">
          <h1 className="mb-5 text-xl">Post Types <i className="fa-solid fa-list fa-sm"></i></h1>
          <div className="flex gap-10">
            <div className="relative">
              <h2 className="mb-2">Post Privacy</h2>
              <select
                value={privacy} 
                onChange={handlePrivacyChange} 
                className="w-full p-3 bg-gray-100 rounded-lg shadow-lg focus:outline-none focus:ring-2 focus:ring-red-300"
              >
                <option value="PUBLIC">Public <i className="fa-solid fa-eye"></i></option>
                <option value="FRIENDS">Friends<i className="fa-solid fa-user-group"></i></option>
                <option value="PRIVATE">Private <i className="fa-solid fa-lock"></i></option>
              </select>
            </div>
            <div className="relative">
              <h2 className="mb-2">Post Category</h2>
              <select
                value={category}
                onChange={handleCategoryChange}
                className="w-full p-3 bg-gray-100 rounded-lg shadow-lg focus:outline-none focus:ring-2 focus:ring-red-300"
              >
                {categories.map((category, index) => (
                  <option key={index} value={category}>{category}</option>
                ))}
              </select>
            </div>
          </div>
        </div>
        <div className="flex flex-row-reverse">
          <button className="bg-red-500 mb-5 w-20 text-white p-2 rounded-lg mt-2 shadow">
            Post
          </button>
        </div>
      </div>
    </div>
  );
};

export default CreatePost;
