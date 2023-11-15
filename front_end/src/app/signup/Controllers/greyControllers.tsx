// 'use client';
//
//
// import {useState} from "react";
//
// function handlePost(payload: any) {
//     const [data, setData] = useState(null);
//     const [error, setError] = useState(null);
//     const [loading, setLoading] = useState(false);
//
//     fetch('http://localhost:8080/signup', {
//         method: 'POST',
//         headers: {'Content-Type': 'application/json'},
//         body: JSON.stringify(payload)
//     }).then((response:any) => {
//         setData(response);
//     }).catch((error:any) => {
//         setError(error);
//         setLoading(false);
//     })
//     return {data, error, loading};
// }
//
//
// export default handlePost;


