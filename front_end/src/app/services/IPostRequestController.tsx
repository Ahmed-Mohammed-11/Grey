interface IPostRequestController {
    sendPostRequest(payload: Object, endpoint: string): Promise<Response>
}

