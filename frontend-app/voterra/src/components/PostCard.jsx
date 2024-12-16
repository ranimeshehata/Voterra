import { useEffect, useState } from "react";
import { useRecoilState } from "recoil";
import { userState } from "../recoil/atoms";

const PostCard = ({post}) => {
    const [totalVotes,setTotalVotes]=useState(0);
    const [votedPoll,setVotedPoll]=useState(-1);
    const [voted,setVoted]=useState(false);
    const [user,setUser]=useRecoilState(userState);
    const [postMenu,setPostMenu]=useState(false);
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
    },[]);
    function vote(){
        if(voted){
            return;
        }
        setVoted(true);
        setTotalVotes(prev=>prev+1);
        
        //backend call
    }
    return ( 
        <div className="shadow-xl rounded-lg p-5 flex flex-col gap-3 font-[nunito] relative">
            <div className="cursor-pointer absolute top-8 right-8">
                <i onClick={()=>setPostMenu(prev=>!prev)} class="fa-solid fa-ellipsis-vertical"></i>
                <div className={`${postMenu?"block":"hidden"} shadow-lg border-2  absolute w-44 bg-white p-3`}>
                    <p className="hover:bg-gray-100">Save Post</p>
                    <p className="hover:bg-gray-100">Delete Post</p>
                </div>
            </div>
            <div id="userInfo" className="flex gap-3 items-center">
                <div className="w-10 h-10 rounded-full flex bg-red-500 text-white justify-center items-center">
                    {post.userName[0]}
                </div>
                <p>posted by {post.userName} &#9679; {post.publishedDate}</p>
            </div>

            <h1 className="text-2xl">{post.postContent}</h1>

            <div id="polls" className="flex flex-col gap-4">
                {post.polls.map((poll,index)=>(
                    <div>
                        <div onClick={vote} className={`${votedPoll==index?"bg-red-100":""} p-2 cursor-pointer hover:shadow-[0_0_15px_2px_rgba(239,68,68,1)] transition-shadow duration-300 shadow-lg rounded-lg bg-gray-100`}>
                            <h2>{poll.pollContent}</h2>
                        </div>
                        <div className={`h-2 rounded-b-lg bg-red-500 transition-all duration-500 ease-in-out`}
                            style={{width: voted ? `${((poll.voters.length / totalVotes) * 100).toFixed()}%` : '0%',}}></div>
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
 
export default PostCard;