/* eslint-disable no-unused-vars */
import { useEffect, useState } from "react";
import PropTypes from 'prop-types';
import { useRecoilState } from "recoil";
import { userState } from "../recoil/atoms";
import useFetch from "../hooks/useFetch";
import { toast } from "react-toastify";

const PostCard = ({post, removePostFromFeed, onSavePost}) => {
    const [totalVotes,setTotalVotes]=useState(0);
    const [votedPoll,setVotedPoll]=useState(-1);
    const [voted,setVoted]=useState(false);
    const [user,setUser]=useRecoilState(userState);
    const [postMenu,setPostMenu]=useState(false);
    const [isSaved, setIsSaved] = useState(post.isSaved);
    const [isReported, setIsReported] = useState(post.isReported);
    const [isDeleted, setIsDeleted] = useState(false);
    const { postSave, deletePost, reportPost } = useFetch();

    useEffect(()=>{
        let x=0;
        const reportedPosts = JSON.parse(localStorage.getItem('reportedPosts')) || [];
        for(let i=0;i<post.polls.length;i++){
            x+=post.polls[i].voters.length;
        }
        setTotalVotes(x);
        for(let i=0;i<post.polls.length;i++){
            if(post.polls[i].voters.includes(user.email)){
                setVoted(true);
                setVotedPoll(i);
            }
        }

        if (user.savedPosts && user.savedPosts.includes(post.id)) {
            setIsSaved(true);
        }

        if (user.reportedPosts && user.reportedPosts.includes(post.id)) {
            setIsReported(true);
        }

        if (reportedPosts.includes(post.id)) {
            setIsReported(true);
        }

    },[post.polls, user.email, user.savedPosts, post.id, user.reportedPosts]);

    const formatDate = (dateString) => {
        const options = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };
        return new Date(dateString).toLocaleDateString(undefined, options);
      };

    const savePost = (id) => {
        const email=user.email;
        const token=localStorage.getItem("token");
        postSave("http://localhost:8080/posts/savePost",{
            token,
            postId:id,
            email:email
        },
        (response, error)=>{
            if (response){
                setIsSaved(true);
                setUser(prevUser => ({
                    ...prevUser,
                    savedPosts: [...prevUser.savedPosts, id]
                }));
                onSavePost(id);
            }
            else{
                console.error(error);
            }
        },
        (error)=>{
            console.error(error);
        });
    };

    const handleReportPost = (postId) => {
        const token = localStorage.getItem('token');
        const reportersId = [user.email];
        console.log(postId);
        console.log(reportersId);
        reportPost("http://localhost:8080/posts/reportPost", {
            postId,
            reportersId,
        },
        (response, error) => {
            if (response) {
                setIsReported(true);
                setUser(prevUser => ({
                    ...prevUser,
                    reportedPosts: [...(prevUser.reportedPosts || []), postId]
                }));
                const reportedPosts = JSON.parse(localStorage.getItem('reportedPosts')) || [];
                reportedPosts.push(postId);
                localStorage.setItem('reportedPosts', JSON.stringify(reportedPosts));
                console.log(response);
            } else {
                console.error(error);
            }
        },
        (error) => {
            console.error(error);
        });
    };

const vote = (pollIndex) => {
        if(voted){
            return;
        }
        setVoted(true);
        setVotedPoll(pollIndex);
        setTotalVotes(prev=>prev+1);
        const token=localStorage.getItem("token");

        postSave("http://localhost:8080/posts/vote",{
            token,
            email:user.email,
            postId:post.id,
            pollIndex:pollIndex,
        },
        (response,error)=>{
            if(response){
                const updatedPolls = post.polls.map((poll, index) => {
                    if (index === pollIndex) {
                        return {
                            ...poll,
                            voters: [...poll.voters, user.email]
                        };
                    }
                    return poll;
                });
                setTotalVotes(prev => prev + 1);
                post.polls = updatedPolls;
                
            }
            else{
                console.error(error);
            }
        },
        (error)=>{
            console.error(error);
        });
    }

    const postDelete = () => {
        const token=localStorage.getItem("token");
        if (!token) {
            toast.error("Session expired. Please log in again.");
            return;
        }

        deletePost("http://localhost:8080/posts/deletePost",{
            token:token,
            post: post
        },
        (response, error)=>{
            if(response){
                setIsDeleted(true);
                removePostFromFeed(post.id);
            }
            else{
                console.error(error);
            }
        },
        (error)=>{
            console.error(error);
        });
    };

    
    
    return (
        <div className="shadow-xl rounded-lg p-5 flex flex-col gap-3 font-[nunito] relative">
            <div className="cursor-pointer absolute top-8 right-8">
                <i onClick={()=>setPostMenu(prev=>!prev)} className="fa-solid fa-ellipsis-vertical"></i>
                <div className={`${postMenu?"block":"hidden"} shadow-lg border-2  absolute w-44 bg-white p-3`}>
                    <p
                        className={`hover:bg-gray-100 ${isSaved ? 'cursor-not-allowed' : 'cursor-pointer'}`}
                        onClick={() => !isSaved && savePost(post.id)}
                    >
                        {isSaved ? 'Post Saved' : 'Save Post'}
                    </p>
                    { post.userEmail == user.email  && (<p className="hover:bg-gray-100" onClick={postDelete}>Delete Post</p>
                    )}
                    {post.userEmail !== user.email && (
                        <p 
                            className={`hover:bg-gray-100 ${isReported ? 'cursor-not-allowed' : 'cursor-pointer'}`}
                            onClick={() => !isReported && handleReportPost(post.id)}
                        >
                            {isReported ? 'Post Reported' : 'Report Post'}
                        </p>
                    )}
                </div>
            </div>
            <div id="userInfo" className="flex gap-3 items-center">
                <div className="w-10 h-10 rounded-full flex bg-red-500 text-white justify-center items-center">
                    <p>{post.userName[0].toUpperCase()}</p>
                </div>
                <p>posted by <strong style={{ fontSize: '1.1rem' }}> {post.userName} </strong> &#9679; <span style={{ fontSize: '0.9rem', fontWeight: 'bold'}}>{formatDate(post.publishedDate)}</span></p>
                </div>

            <h1 className="text-2xl">{post.postContent}</h1>

            <div id="polls" className="flex flex-col gap-4">
                {post.polls.map((poll, index)=>(
                    <div key={index}>
                        <div onClick={() => vote(index)} className={`${votedPoll==index?"bg-red-100":""} p-2 ${voted? "cursor-not-allowed":"cursor-pointer"} hover:shadow-[0_0_15px_2px_rgba(239,68,68,1)] transition-shadow duration-300 shadow-lg rounded-lg bg-gray-100`} >
                            <h2>{poll.pollContent}</h2>
                        </div>
                        <div className={`h-2 rounded-b-lg bg-red-500 transition-all duration-500 ease-in-out`}
                            style={{width: voted ? `${((poll.voters.length / totalVotes) * 100).toFixed()}%` : '0%',}}>
                        </div>
                        <p>{voted ? `${((poll.voters.length / totalVotes) * 100).toFixed()}%` : '0%'}</p>

                    </div>
                ))}
            </div>
            <p>{totalVotes} voters</p>
            <div>
                <p className="p-2 bg-gray-200 rounded-full w-fit">{post.category}</p>
            </div>
            {/* {user.userType === 'ADMIN' && post.reportersCount > 0 && (
                <div>
                    <p className="p-2 bg-red-200 rounded-full w-fit">Reported by {post.reportersCount} {post.reportersCount > 1 ? 'users' : 'user'}</p>
                    </div>
                )} */}
        </div>
     );
}
PostCard.propTypes = {
    removePostFromFeed: PropTypes.func,
    onSavePost: PropTypes.func,
    post: PropTypes.shape({
        id: PropTypes.string.isRequired,
        polls: PropTypes.arrayOf(
            PropTypes.shape({
                voters: PropTypes.arrayOf(PropTypes.string).isRequired,
                pollContent: PropTypes.string.isRequired,
            })
        ).isRequired,
        userName: PropTypes.string.isRequired,
        publishedDate: PropTypes.string.isRequired,
        postContent: PropTypes.string.isRequired,
        category: PropTypes.string.isRequired,
        userEmail: PropTypes.string.isRequired,
        isSaved: PropTypes.bool,
        isReported: PropTypes.bool
    }).isRequired,

};

export default PostCard;
