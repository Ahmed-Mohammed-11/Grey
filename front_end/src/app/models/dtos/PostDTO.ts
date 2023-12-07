import Feeling from "@/app/models/dtos/Feeling";

class PostDTO {
    id?: string;
    postText!: string;
    postFeelings!: Array<Feeling>;
}

export default PostDTO;