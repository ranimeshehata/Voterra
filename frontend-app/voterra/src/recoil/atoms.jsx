import { atom } from 'recoil';

export const userState = atom({
  key: 'userState',
  default: {
    ...JSON.parse(localStorage.getItem('user')) || {},
  reportedPosts: JSON.parse(localStorage.getItem('user'))?.reportedPosts || [],
  },
});


export const isAuthenticatedState = atom({
  key: 'isAuthenticatedState',
  default: !!localStorage.getItem('token'),
});
export const currFilter = atom({
  key: 'currFilter',
  default: "all",
});
export const currSearch = atom({
  key: 'currSearch',
  default: "",
});
