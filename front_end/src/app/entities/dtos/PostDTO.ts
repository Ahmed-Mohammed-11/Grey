import Feeling from "@/app/entities/dtos/Feeling";

class PostDTO {
    id?: string;
    postText!: string;
    postFeelings!: Array<Feeling>;
}

export default PostDTO;