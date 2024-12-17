/* eslint-disable no-unused-vars */
import { useEffect, useState } from "react";
import PropTypes from 'prop-types';
import PostCard from "./PostCard";


const PostContainer = ({posts, removePostFromFeed, onSavePost}) => {
    const [postList, setPostList] = useState(posts);

    useEffect(()=>{
        setPostList(posts);
    },[posts])

    return ( 
        <div className="flex flex-col gap-4">
            {posts.map((post)=>(
                <PostCard
                    key={post.id} 
                    post={post} 
                    removePostFromFeed={removePostFromFeed} 
                    onSavePost={onSavePost}
                />
            ))}
        </div>
     );
}
PostContainer.propTypes = {
    posts: PropTypes.array,
    removePostFromFeed: PropTypes.func,
    onSavePost: PropTypes.func
    
};

export default PostContainer;
