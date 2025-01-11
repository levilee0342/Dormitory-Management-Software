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
import { IResponseData, Region } from "../../types";
import { onError } from "../../utils/error-handlers";
import useAxiosIns from "../../hooks/useAxiosIns";
import { SubmitHandler, useForm } from "react-hook-form";

type UpdateInputs = {
  name: string;
  sex: string;
};

export default function UpdateRegionModal(props: {
  isOpen: boolean;
  onClose: () => void;
  region: Region;
}) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<UpdateInputs>();

  const onSubmit: SubmitHandler<UpdateInputs> = async (data) => {
    await updateMutation.mutateAsync(data);
    props.onClose();
  };

  const axios = useAxiosIns();
  const queryClient = useQueryClient();

  const updateMutation = useMutation({
    mutationFn: (params: UpdateInputs) =>
      axios.put<IResponseData<Region>>(
        `/api/v1/regions/${props.region.id}`,
        params
      ),
    onError,
    onSuccess(data) {
      toast.success(data.data?.message);
      queryClient.invalidateQueries(["fetch/regions"]);
    },
  });

  return (
    <>
      <Modal size="2xl" isOpen={props.isOpen} onClose={props.onClose}>
        <ModalContent>
          {() => (
            <>
              <ModalHeader className="flex flex-col gap-1">
                Cập nhật dãy phòng
              </ModalHeader>
              <ModalBody>
                <div className="flex flex-col gap-4 w-full">
                  <Input
                    defaultValue={props.region.name}
                    errorMessage={errors.name?.message}
                    {...register("name", {
                      required: "Tên dãy phòng không được để trống",
                    })}
                    variant="bordered"
                    size={"md"}
                    label="Tên dãy phòng"
                  />
                  <Select
                    errorMessage={errors.sex?.message}
                    defaultSelectedKeys={[props.region.sex]}
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
                  Cập nhật
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  );
}
