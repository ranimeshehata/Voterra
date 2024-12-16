import { useEffect } from "react";
import PropTypes from 'prop-types';
import { mockPosts } from "../mockdata";
import PostCard from "./PostCard";

const PostContainer = ({posts}) => {
    useEffect(()=>{
        console.log(posts);
    },[posts])
    return ( 
        <div className="flex flex-col gap-4">
            {mockPosts.map((post)=>(
                <PostCard key={post.id} post={post}/>
            ))}
        </div>
     );
}
PostContainer.propTypes = {
    posts: PropTypes.array.isRequired,
};

export default PostContainer;
