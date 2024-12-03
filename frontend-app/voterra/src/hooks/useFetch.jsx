import { useState } from "react";

function useFetch() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchData = async (url, options = {}, onComplete,onError) => {
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
      const result = await response.json();
      console.log(result);
      setData(result);
      if (onComplete) {
        onComplete(result, null);
      }
    } catch (err) {
      setError(err.message);
      onError();
      console.log(err.message);
      if (onComplete) {
        onComplete(null, err);
      }
    } finally {
      setLoading(false);
    }
  };
  

  const get = (url, onComplete) => {
    fetchData(url, { method: "GET" }, onComplete);
  };
  const postSignout = (url,options={}, body, onComplete,onError) => {
    fetchData(
      url,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization":"Bearer "+options.headers.Authorization
        },
      },
      onComplete,onError
    );
  };

  const post = (url, body, onComplete,onError) => {
    fetchData(
      url,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      },
      onComplete,onError
    );
  };

  return { data, loading, error, get, post,postSignout };
}

export default useFetch;