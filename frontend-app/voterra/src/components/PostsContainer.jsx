import { useEffect } from "react";
import { mockPosts } from "../mockdata";
import PostCard from "./PostCard";

const PostContainer = ({posts}) => {
    useEffect(()=>{
        console.log(posts);
    },[])
    return ( 
        <div className="flex flex-col gap-4">
            {posts.map((post)=>(
                <PostCard post={post}/>
            ))}
        </div>
     );
}
 
export default PostContainer;