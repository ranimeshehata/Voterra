import { useState } from "react";

function useFetch() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchData = async (url, options = {}, onComplete) => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(url, options);
      if (!response.ok) {
        const error = new Error(`HTTP error! Status: ${response.status}`);
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

  const post = (url, body, onComplete) => {
    fetchData(
      url,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      },
      onComplete
    );
  };

  return { data, loading, error, get, post };
}

export default useFetch;