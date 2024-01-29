import {toast, ToastOptions} from "react-toastify";

export const toastStyleTopRight: ToastOptions = {
    position: "top-right",
    autoClose: 3000,
    theme: "colored",
    hideProgressBar: true
}

async function toastResponse(response: Promise<Response>) {
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

export default toastResponse;