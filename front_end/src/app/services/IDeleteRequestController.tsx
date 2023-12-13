interface IDeleteRequestController{
    sendDeleteRequest(payload: Object, endpoint: string) : Promise<Response>
}