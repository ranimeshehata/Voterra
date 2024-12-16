/* eslint-disable no-unused-vars */
import { useEffect, useState } from "react";
import PropTypes from 'prop-types';
import { useRecoilState } from "recoil";
import { userState } from "../recoil/atoms";
import useFetch from "../hooks/useFetch";

const PostCard = ({post}) => {
    const [totalVotes,setTotalVotes]=useState(0);
    const [votedPoll,setVotedPoll]=useState(-1);
    const [voted,setVoted]=useState(false);
    const [user,setUser]=useRecoilState(userState);
    const [postMenu,setPostMenu]=useState(false);
    const [isSaved, setIsSaved] = useState(false);
    const { postSave } = useFetch();


    useEffect(()=>{
        let x=0;
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
    },[post.polls, user.email]);

    const formatDate = (dateString) => {
        const options = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };
        return new Date(dateString).toLocaleDateString(undefined, options);
      };

    const savePost = (id) => {
        console.log("saving post with id: ", id);
        const email=user.email;
        const token=localStorage.getItem("token");
        postSave("http://localhost:8080/posts/savePost",{
            token,
            postId:id,
            email:email
        },
        (response, error)=>{
            if(response){
                setIsSaved(true);
                console.log("Post saved successfully");
                console.log(response);
                console.log("bravo");
            }
            else{
                console.error("Error saving post:", error);
                console.log("bad");
            }
        },
        (error)=>{
            console.error("Error saving post:", error);
        });

        console.log("Post saved with id: ", id);

    }

    function vote(){
        if(voted){
            return;
        }
        setVoted(true);
        setTotalVotes(prev=>prev+1);
        const token=localStorage.getItem("token");
        //backend call
        postSave("http://localhost:8080/posts/vote",{
            token,
            email:user.email,
            postId:post.id,
            pollIndex:votedPoll,
        },
        (response,error)=>{
            if(response){
                console.log("Vote successful");
            }
            else{
                console.error("Error voting:", error);
            }
        },
        (error)=>{
            console.error("Error voting:", error);
        });
    }

    console.log(post);
    console.log(post.userName);
    return (
        <div className="shadow-xl rounded-lg p-5 flex flex-col gap-3 font-[nunito] relative">
            <div className="cursor-pointer absolute top-8 right-8">
                <i onClick={()=>setPostMenu(prev=>!prev)} className="fa-solid fa-ellipsis-vertical"></i>
                <div className={`${postMenu?"block":"hidden"} shadow-lg border-2  absolute w-44 bg-white p-3`}>
                    <p
                        className={`hover:bg-gray-100 ${isSaved ? 'cursor-not-allowed' : 'cursor-pointer'}`}
                        onClick={() => savePost(post.id)}
                        style={isSaved ? { pointerEvents: 'none' } : {}}
                    >
                        {isSaved ? 'Post Saved' : 'Save Post'}
                    </p>
                    <p className="hover:bg-gray-100">Delete Post</p>
                </div>
            </div>
            <div id="userInfo" className="flex gap-3 items-center">
                <div className="w-10 h-10 rounded-full flex bg-red-500 text-white justify-center items-center">
                    <p>{post.userName[0]}</p>
                </div>
                <p>posted by {post.userName} &#9679; {formatDate(post.publishedDate)}</p>
            </div>

            <h1 className="text-2xl">{post.postContent}</h1>

            <div id="polls" className="flex flex-col gap-4">
                {post.polls.map((poll,index)=>(
                    <div key={index}>
                        <div onClick={vote} className={`${votedPoll==index?"bg-red-500":""} p-2 cursor-pointer hover:shadow-[0_0_15px_2px_rgba(239,68,68,1)] transition-shadow duration-300 shadow-lg rounded-lg bg-gray-100`}>
                            <h2>{poll.pollContent}</h2>
                        </div>
                        <div className={`h-2 rounded-b-lg bg-red-500 transition-all duration-500 ease-in-out`}
                            style={{width: voted ? `${((poll.voters.length / totalVotes) * 100).toFixed()}%` : '0%',}}>
                        </div>
                    </div>
                ))}
            </div>
            <p>{totalVotes} voters</p>
            <div>
                <p className="p-2 bg-gray-200 rounded-full w-fit">{post.category}</p>
            </div>
        </div>
     );
}
PostCard.propTypes = {
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
    }).isRequired,
};

export default PostCard;
