import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Button,
  Input,
} from "@nextui-org/react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { IResponseData, RoomType } from "../../types";
import { onError } from "../../utils/error-handlers";
import useAxiosIns from "../../hooks/useAxiosIns";
import { SubmitHandler, useForm } from "react-hook-form";

type UpdateRoomTypeInputs = {
  name: string;
  capacity: number;
  price: number;
};

export default function UpdateRoomTypeModal(props: {
  isOpen: boolean;
  onClose: () => void;
  roomType: RoomType;
}) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<UpdateRoomTypeInputs>();

  const onSubmit: SubmitHandler<UpdateRoomTypeInputs> = async (data) => {
    await updateMutation.mutateAsync(data);
    props.onClose();
  };

  const axios = useAxiosIns();
  const queryClient = useQueryClient();

  const updateMutation = useMutation({
    mutationFn: (params: UpdateRoomTypeInputs) =>
      axios.put<IResponseData<RoomType>>(
        `/api/v1/room-types/${props.roomType.id}`,
        params
      ),
    onError,
    onSuccess(data) {
      toast.success(data.data?.message);
      queryClient.invalidateQueries(["fetch/room-types"]);
    },
  });

  return (
    <>
      <Modal size="2xl" isOpen={props.isOpen} onClose={props.onClose}>
        <ModalContent>
          {() => (
            <>
              <ModalHeader className="flex flex-col gap-1">
                Sửa loại phòng
              </ModalHeader>
              <ModalBody>
                <div className="flex flex-col gap-4 w-full">
                  <Input
                    defaultValue={props.roomType.name}
                    errorMessage={errors.name?.message}
                    {...register("name", {
                      required: "Tên loại phòng là bắt buộc",
                    })}
                    variant="bordered"
                    size={"md"}
                    label="Tên loại phòng"
                  />
                  <Input
                    defaultValue={props.roomType.capacity.toString()}
                    errorMessage={errors.capacity?.message}
                    {...register("capacity", {
                      required: "Sức chứa là bắt buộc",
                      min: {
                        value: 1,
                        message: "Sức chứa phải lớn hơn 0",
                      },
                      max: {
                        value: 12,
                        message: "Sức chứa phải nhỏ hơn 13",
                      },
                    })}
                    type="number"
                    variant="bordered"
                    size={"md"}
                    label="Sức chứa"
                  />
                  <Input
                    defaultValue={props.roomType.price.toString()}
                    errorMessage={errors.price?.message}
                    {...register("price", {
                      required: "Giá phòng là bắt buộc",
                      min: {
                        value: 1000,
                        message: "Giá phòng phải lớn hơn 1000",
                      },
                    })}
                    variant="bordered"
                    size={"md"}
                    label="Giá phòng"
                  />
                </div>
              </ModalBody>
              <ModalFooter>
                <Button
                  isLoading={updateMutation.isLoading}
                  variant="light"
                  onPress={props.onClose}
                >
                  Đóng
                </Button>
                <Button
                  isLoading={updateMutation.isLoading}
                  color="primary"
                  onClick={handleSubmit(onSubmit)}
                >
                  Tạo
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  );
}
