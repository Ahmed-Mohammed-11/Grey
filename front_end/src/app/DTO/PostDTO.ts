import Feeling from "@/app/DTO/Feeling";

class PostDTO {
    id!: string;
    postText!: string;
    postFeelings!: Set<Feeling>;
}

export default PostDTO;