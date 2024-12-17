import { useState } from "react";

function useFetch() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchData = async (url, options = {}, onComplete, onError) => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(url, options);
      if (!response.ok) {
        const errorJson = await response.json(); 
        const error = new Error(errorJson.message || `HTTP error! Status: ${response.status}`);
        error.status = response.status;
        throw error;
      }
      const contentType = response.headers.get("content-type");
      const result = contentType && contentType.includes("application/json")
        ? await response.json()
        : await response.text();
      setData(result);
      if (onComplete) {
        onComplete(result, null);
      }
    } catch (err) {
      setError(err.message);
      onError(err);
      console.log(err.message);
      if (onComplete) {
        onComplete(null, err);
      }
    } finally {
      setLoading(false);
    }
  };
  
  const get = (url, onComplete, onError) => {
    fetchData(url, { method: "GET" }, onComplete, onError);
  };

  const postSignout = (url,token, onComplete, onError) => {
    fetchData(
      url,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization:"Bearer "+ token.token,
        },
      },
      onComplete,
      onError
    );
  };

  const post = (url, body, onComplete, onError) => {
    fetchData(
      url,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      },
      onComplete,
      onError
    );
  };

  const postCreate = (url, body, onComplete, onError) => {
    fetchData(
      url,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization:"Bearer "+ body.token
        },
        body: JSON.stringify(body),
      },
      onComplete,
      onError
    );
  };

  const postSave = (url, body, onComplete, onError) => {
    fetchData(
      url,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization:"Bearer "+ body.token
        },
        body: JSON.stringify(body),
      },
      onComplete,
      onError
    );
  };

  const deletePost = (url, body, onComplete, onError) => {
    fetchData(
      url, 
      {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization:"Bearer "+ body.token
        },
        body: JSON.stringify(body.post),
      }, onComplete, onError);
  };

  return { 
    data, 
    loading, 
    error, 
    get, 
    post,
    postSignout,
    postCreate,
    postSave,
    deletePost
  };
}

export default useFetch;
