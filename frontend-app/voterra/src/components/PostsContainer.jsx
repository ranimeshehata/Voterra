import { useEffect } from "react";
import { mockPosts } from "../mockdata";
import PostCard from "./PostCard";

const PostContainer = ({posts}) => {
    useEffect(()=>{
        console.log(mockPosts);
        
    },[])
    return ( 
        <div className="flex flex-col gap-4">
            {mockPosts.map((post)=>(
                <PostCard post={post}/>
            ))}
        </div>
     );
}
 
export default PostContainer;