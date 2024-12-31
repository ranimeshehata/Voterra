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
}
export async function fetchUserProfilePosts(page,email) {
  const token = localStorage.getItem("token");
  try {
    const response = await fetch(`http://localhost:8080/posts/userContent?email=${email}&page=${page}`, {
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
    return data;
  } catch (error) {
    console.error(error.message);
  }
}

export async function fetchPosts(page) {
    const token = localStorage.getItem("token");
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
      return data;
    } catch (error) {
      console.error(error.message);
    }
}

export async function fetchSavedPosts(page) {
    const token = localStorage.getItem("token");
    try {
      const response = await fetch(`http://localhost:8080/posts/getSavedPosts?page=${page}`, {
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
      return data;
    } catch (error) {
      console.error(error.message);
    }
}

export async function fetchReportedPosts(page) {
    const token = localStorage.getItem("token");
    try {
      const response = await fetch(`http://localhost:8080/posts/getReportedPosts?page=${page}`, {
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
      console.log("Raw data:", data);
      const transformedData = data.map(item => ({
        id: item.post.id,
        userEmail: item.post.userEmail,
        userName: item.post.userName,
        postContent: item.post.postContent,
        category: item.post.category,
        privacy: item.post.privacy,
        polls: item.post.polls.map(poll => ({
          pollContent: poll.pollContent,
          voters: poll.voters
      })),
        publishedDate: item.post.publishedDate,
        reportersCount: item.numberOfReports
    }));

    console.log("Transformed data:", transformedData);
    return transformedData;
      // return data;
      // return data.map(post => ({
      //   ...post,
      //   reportersCount: post.reportersId ? post.reportersId.length : 0
      // }));
    } catch (error) {
      console.error(error.message);
    }
}