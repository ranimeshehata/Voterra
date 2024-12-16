/* eslint-disable no-unused-vars */
import { useEffect, useState } from "react";
import CreatePost from "./CreatePost";
import PostContainer from "./PostsContainer";
import { fetchPosts } from "../voterraUtils/PostUtils";

const MainSection = () => {
    const [posts,setPosts]=useState([]);

    useEffect(()=>{
        async function getPosts(){
            let data=await fetchPosts(0);
            setPosts(data);
        }
        getPosts();
    },[])

    const addPost = (newPost) => {
        setPosts([newPost, ...posts]);
      };
    
    return ( 
        <div className="flex flex-col gap-10">
            <CreatePost addPost={addPost} />
            <PostContainer posts={posts}/>
        </div>
     );
}
 
export default MainSection;