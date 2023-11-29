import Feeling from "@/app/models/dtos/Feeling";

class PostDTO {
    id!: string;
    postText!: string;
    postFeelings!: Set<Feeling>;
}

export default PostDTO;