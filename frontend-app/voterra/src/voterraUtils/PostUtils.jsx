/* eslint-disable no-unused-vars */
export const createPost=async(content,polls,category,privacy,username,email)=>{
  const today = new Date();
  const formattedDate = today.toISOString();
    let postObj={};
    
    postObj.postContent = content;
    postObj.userEmail = email;
    postObj.category = category;
    postObj.privacy = privacy;
    postObj.userName = username;
    postObj.publishedDate=formattedDate;
    postObj.polls=[];
    for(let i=0; i<polls.length; i++){
        let temp={
            pollContent:polls[i],
            voters:[]
        }
        postObj.polls.push(temp);
    }
    const token = localStorage.getItem("token");
    let x=await createPostBack(postObj,token);
}

const createPostBack = async (postData, token) => {
  console.log(postData);
  
  try {
      const response = await fetch('http://localhost:8080/posts/createPost', {
          method: 'POST',
          headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json', 
          },
          body: JSON.stringify(postData),
      });

      if (!response.ok) {
          throw new Error('Failed to create post');
      }

      const data = await response.json();
      console.log('Post created:', data);
  } catch (error) {
      console.error('Error creating post:', error);
  }
};
export async function fetchPosts(page) {
    const token = localStorage.getItem("token");
    console.log(token);
    try {
      const response = await fetch(`http://localhost:8080/posts/homepage?page=${page}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization:"Bearer "+ token
        },
      });
  
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'failed');
      }
  
      const data = await response.json();
      console.log( data);
      return data;
    } catch (error) {
      console.error('Error fetching posts:', error.message);
    }
}