import {toast, ToastOptions} from "react-toastify";

export const SIGNUP_PANEL_TEXT = 'A social media platform that provides anonymous\n' +
    '                        experience for users to freely express their feelings and opinions, participate in events, and\n' +
    '                        much more.'


export const LOGIN_PANEL_TEXT = 'Welcome Back!'
export const VERIFY_PANEL_TEXT = 'Please verify your email'

const toastStyleTopRight: ToastOptions = {
    position: "top-right",
    autoClose: 2000,
    theme: "colored",
    hideProgressBar: true
}

export async function toastResponse(response: Promise<Response>) {
    try {
        await toast.promise(response.then(res => {
                if (res.status === 200 || res.status === 201) {
                    res.text().then((data: any) => {
                        toast.success(data, toastStyleTopRight);
                    });
                } else {
                    res.text().then((data: any) => {
                        toast.error(data, toastStyleTopRight);
                    });
                }
            }, (err) => { console.log(err) }),
            {
                pending: 'Wait a moment with me ...',
                error: 'Server took too long to respond',
            }, toastStyleTopRight
        );
    } catch (error) {
        console.error(error);
    }
}