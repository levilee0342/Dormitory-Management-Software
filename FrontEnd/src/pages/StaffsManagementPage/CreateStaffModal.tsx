import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Button,
  Input,
  Select,
  SelectItem,
} from "@nextui-org/react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { IResponseData, IUser } from "../../types";
import { onError } from "../../utils/error-handlers";
import useAxiosIns from "../../hooks/useAxiosIns";
import { SubmitHandler, useForm } from "react-hook-form";
type CreateUserInputs = {
  id: string;
  first_name: string;
  last_name: string;
  date_of_birth: string;
  phone?: string;
  address: string;
  sex: string;
};

export default function CreateUserModal(props: {
  isOpen: boolean;
  onClose: () => void;
}) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateUserInputs>();

  const onSubmit: SubmitHandler<CreateUserInputs> = async (data) => {
    await createUserMutation.mutateAsync(data);
    props.onClose();
  };

  const axios = useAxiosIns();
  const queryClient = useQueryClient();

  const createUserMutation = useMutation({
    mutationFn: (params: CreateUserInputs) =>
      axios.post<IResponseData<IUser>>(`/api/v1/staffs`, params),
    onError,
    onSuccess(data) {
      toast.success(data.data?.message);
      queryClient.invalidateQueries(["fetch/staffs-management"]);
    },
  });

  return (
    <>
      <Modal size="2xl" isOpen={props.isOpen} onClose={props.onClose}>
        <ModalContent>
          {() => (
            <>
              <ModalHeader className="flex flex-col gap-1">
                Tạo nhân viên mới
              </ModalHeader>
              <ModalBody>
                <div className="w-full h-full flex gap-4">
                  <div className="flex flex-col gap-4 w-full">
                    <Input
                      errorMessage={errors.id?.message}
                      {...register("id", {
                        required: "Mã nhân viên là bắt buộc",
                      })}
                      variant="bordered"
                      size={"md"}
                      label="Mã nhân viên"
                    />
                    <Input
                      errorMessage={errors.first_name?.message}
                      {...register("first_name", {
                        required: "Tên là bắt buộc",
                      })}
                      variant="bordered"
                      size={"md"}
                      label="Tên"
                    />
                    <Input
                      errorMessage={errors.last_name?.message}
                      {...register("last_name", {
                        required: "Họ là bắt buộc",
                      })}
                      variant="bordered"
                      size={"md"}
                      label="Họ"
                    />
                  </div>
                  <div className="flex flex-col gap-4 w-full">
                    <Input
                      errorMessage={errors.phone?.message}
                      {...register("phone", {
                        required: false,
                        pattern: {
                          value: /^\d{10}$/,
                          message: "Số điện thoại không hợp lệ",
                        },
                      })}
                      variant="bordered"
                      size={"md"}
                      label="Số điện thoại (tùy chọn)"
                    />
                    <Input
                      {...register("address", {
                        required: "Địa chỉ là bắt buộc",
                      })}
                      variant="bordered"
                      size={"md"}
                      label="Địa chỉ"
                    />
                    <Input
                      errorMessage={errors.date_of_birth?.message}
                      {...register("date_of_birth", {
                        required: "Ngày sinh là bắt buộc",
                      })}
                      max={new Date().toISOString().split("T")[0]}
                      placeholder="dd/mm/yyyy"
                      date-format="dd/mm/yyyy"
                      type="date"
                      variant="bordered"
                      size={"md"}
                      label="Ngày sinh"
                    />
                    <Select
                      errorMessage={errors.sex?.message}
                      {...register("sex", {
                        required: "Giới tính là bắt buộc",
                      })}
                      variant="bordered"
                      label="Giới tính"
                      size="md"
                    >
                      <SelectItem key="MALE" value="MALE">
                        Nam
                      </SelectItem>
                      <SelectItem key="FEMALE" value="FEMALE">
                        Nữ
                      </SelectItem>
                      <SelectItem key="OTHER" value="OTHER">
                        Khác
                      </SelectItem>
                    </Select>
                  </div>
                </div>
              </ModalBody>
              <ModalFooter>
                <Button
                  isLoading={createUserMutation.isLoading}
                  variant="light"
                  onPress={props.onClose}
                >
                  Đóng
                </Button>
                <Button
                  isLoading={createUserMutation.isLoading}
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
