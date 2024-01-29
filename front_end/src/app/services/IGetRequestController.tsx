interface IGetRequestController {
    sendGetRequest(endpoint: string): Promise<Response>
}

